describe(`TC-PBI19-PERSIONAL-BOARD-4-FE-1\n 
    Test Scenario : normal
                    - custom status is specific to each board
                    - somsuan views his task and statuses`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.somsuan')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()
    }) ;
    

    it('Open the login page at /login and open the /board page', () => {
        cy.wait(100)
        cy.url().should('contain','/board')
        cy.contains('ITBKK SOMSUAN personal board')
        cy.get('.itbkk-item').should('have.length',1)
    })

    it('[Step 2.1] Should have task title "user2 first task"',()=>{
        cy.get('.itbkk-title').contains('user2 first task').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-assignees','Unassigned')
        cy.get('@item').contains('.itbkk-status',"Doing")
    })

    it('Should have "Manage Status" and click to open the Status list page',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
    })

    it('[Step 2.2] Status table shoud contain "No Status","To Do"," Doing" and "Done".',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-item').should('have.length',4) ;

        cy.get('.itbkk-item').eq(0).as('item')
        cy.get('@item').contains('.itbkk-status-name',"No Status") 

        cy.get('.itbkk-item').eq(1).as('item')
        cy.get('@item').contains('.itbkk-status-name',"To Do")

        cy.get('.itbkk-item').eq(2).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Doing")

        cy.get('.itbkk-item').eq(3).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Done")
    })

})