package com.example.integratedbackend.JWT;

import com.example.integratedbackend.DTO.DTOV3.CollabDTO;
import com.example.integratedbackend.ErrorHandle.AccessRightNotAllow;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.ErrorHandle.NonCollaboratorException;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.AccessRight;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.CollabRepositoriesV3;
import com.example.integratedbackend.Service.ServiceV3.*;
import com.example.integratedbackend.Service.UserService;
import com.example.integratedbackend.Users.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private CollabService collabService;
    @Autowired
    private TaskServiceV3 taskServiceV3;
    @Autowired
    private StatusServiceV3 statusServiceV3;
    @Autowired
    private VisibilityService visibilityService;
    @Autowired
    private CollabRepositoriesV3 collabRepositoriesV3;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/token")) {
            filterChain.doFilter(request, response);
            return;
        } else {
            String boardId = extractBoardIdFromURI(request.getRequestURI());
            try {
                boolean isPublic = boardService.isBoardPublic(boardId);
                if (isPublic && request.getMethod().equals("GET")) {
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

//        if (!isPublic) {
//            if (requestMethod.equals("GET")) {
//                throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Board is private, Only Collaborator and Owner can access");
//            }else {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
//            }
//        }else {
//            if (!requestMethod.equals("GET")) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
//            }
//        }


        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);

            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (IllegalArgumentException e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unable to get JWT Token", request.getRequestURI());
                return;
            } catch (ExpiredJwtException e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired", request.getRequestURI());
                return;
            } catch (MalformedJwtException e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is not well-formed", request.getRequestURI());
                return;
            } catch (SignatureException e) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has been tampered with", request.getRequestURI());
                return;
            } catch (Exception e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error during JWT processing", request.getRequestURI());
                return;
            }

        } else if (authHeader == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing", request.getRequestURI());
            return;
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header must start with Bearer", request.getRequestURI());
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token", request.getRequestURI());
                return;
            }

            if (request.getRequestURI().startsWith("/v3/boards")) {
                try {
                    handleRequest(request);
                } catch (ItemNotFoundException e) {
                    sendErrorResponse(response, 404, e.getMessage(), request.getRequestURI());
                    return;
                } catch (NonCollaboratorException e) {
                    sendErrorResponse(response, 403, e.getMessage(), request.getRequestURI());
                    return;
                } catch (ResponseStatusException e) {
                    sendErrorResponse(response, e.getStatusCode().value(), e.getMessage(), request.getRequestURI());
                    return;
                } catch (Exception e) {
                    sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error during JWT processing", request.getRequestURI());
                    return;
                }
            }
        } else if (username == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT Token does not contain a valid username", request.getRequestURI());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractBoardIdFromURI(String requestURI) {
        String[] parts = requestURI.split("/");
        if (parts.length >= 4 && parts[2].equals("boards")) {
            return parts[3];
        }
        return null;
    }


    private void handleRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Missing or invalid Authorization header");
        }
        String jwt = authHeader.substring(7);

        String requestMethod = request.getMethod();
        String currentUser = (String) jwtUtil.extractAllClaims(jwt).get("oid");
        String boardId = extractBoardIdFromURI(request.getRequestURI());

        if (boardId == null) {
            return;
        }

        if (!boardService.boardExists(boardId)) {
            if (!requestMethod.equals("GET")) {
                throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Board Not Found");
            }
            return;
        }

        boolean isPublic = boardService.isBoardPublic(boardId);
        boolean isOwner = currentUser != null && boardService.isBoardOwner(boardId, currentUser);
        boolean isCollaborator = currentUser != null && collabService.isCollaborator(boardId, currentUser);

        if (isOwner) {
            return;
        }

        if (isCollaborator) {
            AccessRight accessRight = collabService.getCollab(boardId, currentUser).getAccessRight();

            if (accessRight == AccessRight.READ && requestMethod.equals("GET") || requestMethod.equals("DELETE")) {
                return;
            }
            if (requestMethod.equals("PATCH") && request.getRequestURI().contains("collabs/invitations")) {
                return;
            }

            if (accessRight == AccessRight.WRITE) {
                if (requestMethod.equals("PATCH")) {
                    throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Write access not allowed for this action");
                }
                if (requestMethod.equals("POST") && request.getRequestURI().contains("/collabs")) {
                    throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Write access not allowed for this action");
                }
                if (isAllowedForWriteAccess(requestMethod, request.getRequestURI())) {
                    validateTaskAndStatusExistence(request);
                    return;
                }
                throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Write access not allowed for this action");
            }

            throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Collaborator does not have sufficient permissions");
        }

        if (!isPublic) {
            if (requestMethod.equals("GET")) {
                throw new NonCollaboratorException(HttpStatus.FORBIDDEN, "Board is private, Only Collaborator and Owner can access");
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
            }
        } else {
            if (!requestMethod.equals("GET")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
            }
        }
    }

    private void validateTaskAndStatusExistence(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String boardId = extractBoardIdFromURI(uri);

        if (uri.matches(".*/tasks/\\d+$") && request.getMethod().equals("PUT")) {
            Integer taskId = extractIdFromURI(uri, "/tasks/");
            if (taskId == null || !taskServiceV3.isTaskAvailable(taskId, boardId)) {
                throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Task not found");
            }
        }

        if (uri.matches(".*/statuses/\\d+$") && request.getMethod().equals("PUT")) {
            Integer statusId = extractIdFromURI(uri, "/statuses/");
            if (statusId == null || !statusServiceV3.isStatusAvailable(statusId, boardId)) {
                throw new ItemNotFoundException(HttpStatus.NOT_FOUND, "Status not found");
            }
        }
    }

    private Integer extractIdFromURI(String uri, String segment) {
        try {
            String[] parts = uri.split(segment);
            if (parts.length > 1) {
                String idPart = parts[1].split("/")[0];
                return Integer.parseInt(idPart);
            }
        } catch (NumberFormatException e) {
            throw new ItemNotFoundException(HttpStatus.BAD_REQUEST, "Invalid ID format in URI");
        }
        return null;
    }


    private boolean isAllowedForWriteAccess(String method, String uri) {
        String[] allowedWriteEndpoints = {
                "", "/statuses", "/tasks", "/collabs"
        };

        for (String endpoint : allowedWriteEndpoints) {
            if (uri.contains(endpoint)) {
                return true;
            }
        }
        return method.equals("POST") || method.equals("PUT") || method.equals("DELETE");
    }


    private void sendErrorResponse(HttpServletResponse response, int status, String message, String path) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": \"%s\", \"path\": \"%s\"}", message, path));
    }
}


