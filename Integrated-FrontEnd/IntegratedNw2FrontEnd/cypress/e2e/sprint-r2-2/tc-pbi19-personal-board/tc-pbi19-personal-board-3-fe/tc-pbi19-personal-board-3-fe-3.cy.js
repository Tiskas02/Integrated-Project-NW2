describe(`TC-PBI19-PERSIONAL-BOARD-3-FE-3\n 
    Test Scenario : normal
                    - task is specific to each board
                    - basic task CRUD
                    - refresh somchai page and change the status of the first task to "To Do".`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()
    }) ;
    

    it('[Step 6] Open the login page at /login and open the /board page', () => {
        cy.wait(100)
        cy.url().should('contain','/board')
        cy.contains('Software Development Kanban Board for the course INT222 Integrated Project II at School of Information Technology in 202')
    })

    it('Task table has one task and have task title "user1 first task"',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-item').should('have.length',1) ;

        cy.get('.itbkk-title').contains('user1 first task')
    })

    it('should have button action and button edit.',()=>{
        cy.get('.itbkk-title').contains("user1 first task")
        cy.get('.itbkk-title').contains("user1 first task").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-action').click().as('action')
        cy.get('@action').find('.itbkk-button-edit')
    })

    it('[Step 7] should click the edit button to open a mdal window with task detail and change the status to "To Do".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-title').contains("user1 first task")
        cy.get('.itbkk-title').contains("user1 first task").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-action').click().as('action')
        cy.get('@action').find('.itbkk-button-edit').click()
        cy.wait(100)

        cy.get('.itbkk-modal-task').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-status').select('To Do')
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
    })

    it('Should have "user1 first task","Unassigned" and "To Do".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-assignees','Unassigned')
        cy.get('@item').contains('.itbkk-status',"To Do")
    })

    it('[Step 8] should have button action and button delete.',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-title').contains("user1 first task")
        cy.get('.itbkk-title').contains("user1 first task").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-action').click().as('action')
        cy.get('@action').find('.itbkk-button-delete').as('delete')

        cy.get('@delete').click()
        cy.get('.itbkk-message').contains('Do you want to delete the task number')
        cy.get('.itbkk-message').contains('user1 first task')
        cy.get('.itbkk-button-confirm').contains("Confirm").click()
        cy.wait(100)

        cy.get('.itbkk-item').should('have.length',0) ;
    })
})