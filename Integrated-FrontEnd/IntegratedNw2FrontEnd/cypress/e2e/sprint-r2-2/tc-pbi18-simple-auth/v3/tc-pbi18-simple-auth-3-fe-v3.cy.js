describe(`TC-PBI18-PERSIONAL-BOARD-3-FE-v3\n 
    Test Scenario : fail
                    -  /board/*
                    -  token is invalid (expired / not well-formed / tampered with)`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.sanit')
        cy.get('.itbkk-password').type('ip23/SAN')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/board')
    }) ;

    it('[Step 3.1] Open the login page at /login and open the /board page', () => {
    })

    it('[Step 3.2] Should get statusCode 401 when opening the /board/1 page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v3/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/board/1')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })

    it('[Step 3.3] Should get statusCode 401 when opening the /board/1/task/1/edit page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v3/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/board/1/task/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })
   

    it('[Step 3.4] Should get statusCode 401 when opening the /board/1/status page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v3/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/board/1/status')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })

    it('[Step 3.6] Should get statusCode 401 when opening the /board/1/status/1/edit page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v3/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/board/1/status/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })
    
})