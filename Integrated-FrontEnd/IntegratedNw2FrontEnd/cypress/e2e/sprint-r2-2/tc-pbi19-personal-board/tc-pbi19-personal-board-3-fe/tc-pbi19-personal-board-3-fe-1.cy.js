describe(`TC-PBI19-PERSIONAL-BOARD-3-FE-1\n 
    Test Scenario : normal
                    - task is specific to each board
                    - basic task CRUD
                    - somchai create a new task`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()
    }) ;
    

    it('Open the login page at /login and open the /board page', () => {
        cy.wait(100)
        cy.url().should('contain','/board')
        cy.contains('Software Development Kanban Board for the course INT222 Integrated Project II at School of Information Technology in 202')
    })

    it('should have add task button and click to open task add modal',()=>{
        cy.get('.itbkk-button-add').should('exist').click() ;

        cy.get('.itbkk-modal-task').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-title').type('user1 first task')
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
    })

    it('[Step 1] Should have task title "user1 first task"',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('@item').contains('.itbkk-assignees','Unassigned')
        cy.get('@item').contains('.itbkk-status',"No Status")
    })

    it('Should have "No Description Provided","Unassigned" and "No Status".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').click()
    
        cy.wait(200)
        cy.get('.itbkk-title').contains("user1 first task").click()
        cy.get('.itbkk-description').contains('No Description Provided').as('description')
        cy.get('.itbkk-assignees').contains('Unassigned')
        cy.get('.itbkk-status').contains('No Status')
    })

})