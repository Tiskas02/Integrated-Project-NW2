describe(`TC-PBI19-PERSIONAL-BOARD-2-FE\n 
    Test Scenario : normal
                    - existing user with one board`, () => {

    it('Open the login page at /login and open the /board page', () => {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/board')
        cy.contains('Software Development Kanban Board for the course INT222 Integrated Project II at School of Information Technology in 202')
    })

})