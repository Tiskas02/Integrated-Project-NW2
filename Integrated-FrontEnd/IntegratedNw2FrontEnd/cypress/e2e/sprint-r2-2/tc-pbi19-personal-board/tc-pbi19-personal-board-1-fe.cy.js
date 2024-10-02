describe(`TC-PBI19-PERSIONAL-BOARD-1-FE\n 
    Test Scenario : normal
                    - new use with no board
                    - create personal board`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/board')
    }) ;

    it('Open the login page at /login and open the /board page', () => {
    })

    it('should have a "Create personal board" button and click',()=>{
        cy.get('.itbkk-button-create').should('exist').as('button')
        cy.wait(100) 

        cy.get('@button').click()
    })

    it('[Step 4] should have a new board modal and Type a board name',()=>{
        cy.get('.itbkk-button-create').should('exist').as('button')
        cy.wait(100) 

        cy.get('@button').click()
        cy.wait(100)

        cy.get('.itbkk-modal-new').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-board-name').clear()
        cy.get('@modal').find('.itbkk-board-name').type('Software Development Kanban Board for the course INT222 Integrated Project II at School of Information Technology in 2024')
        cy.get('@modal').find('.itbkk-button-ok').should('exist')
    })

    it('[Step 5] Should handle status 401 with invalid token (tempered or expired) and redirect to /login',()=>{
        // let apiUrl = Cypress.env('apiUrl')
        cy.intercept('POST','http:/intproj23.sit.kmutt.ac.th/ssi1/api/v3/*',{
            statusCode: 401
        }).as('mockAPI')

        cy.get('.itbkk-button-create').should('exist').as('button')
        cy.wait(100) 

        cy.get('@button').click()
        cy.wait(100)

        cy.get('.itbkk-modal-new').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-board-name').clear()
        cy.get('@modal').find('.itbkk-board-name').type('Software Development Kanban Board for the course INT222 Integrated Project II at School of Information Technology in 2024')
        cy.get('@modal').find('.itbkk-button-ok').should('exist').click()
        
        cy.wait('@mockAPI').its('response.statusCode').should('eq',401)
        
        cy.url().should('contain','/login')
    })

    it('[Step 6] Open /board page and redirect to /login',()=>{
        // let apiUrl = Cypress.env('apiUrl')
        //http://localhost:8080/v3/boards
        cy.intercept('GET','http:/intproj23.sit.kmutt.ac.th/ssi1/api/v3/*',{
            statusCode: 401
        }).as('mockAPI')

        cy.visit('/board')
        cy.wait(100)
        cy.wait('@mockAPI').its('response.statusCode').should('eq',401)

        cy.url().should('contain','/login')
    })

    it('[Step 7] should have a new board modal and Type a board name and Click "Create" button',()=>{
        cy.get('.itbkk-button-create').should('exist').as('button')
        cy.wait(100) 

        cy.get('@button').click()
        cy.wait(100)

        cy.get('.itbkk-modal-new').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-board-name').clear()
        cy.get('@modal').find('.itbkk-board-name').type('Software Development Kanban Board for the course INT222 Integrated Project II at School of Information Technology in 2024')
        cy.get('@modal').find('.itbkk-button-ok').should('exist').click()
    })

    it('[Step 7] should redirect to the new board.',()=>{
        cy.contains('Software Development Kanban Board for the course INT222 Integrated Project II at School of Information Technology in 202')
    })
})