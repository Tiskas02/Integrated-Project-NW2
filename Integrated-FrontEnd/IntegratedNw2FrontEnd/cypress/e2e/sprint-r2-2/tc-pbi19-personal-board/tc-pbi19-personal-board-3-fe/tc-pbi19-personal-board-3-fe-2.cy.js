describe(`TC-PBI19-PERSIONAL-BOARD-3-FE-1\n 
    Test Scenario : normal
                    - task is specific to each board
                    - basic task CRUD
                    - user2 create a new board and add a task`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.somsuan')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()
    }) ;
    

    it('[Step 3 and 4] Should have a new board modal and Click "Create" button with default board name',()=>{
        cy.get('.itbkk-button-create').should('exist').as('button')
        cy.wait(100) 

        cy.get('@button').click()
        cy.wait(100)

        cy.get('.itbkk-modal-new').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-button-ok').should('exist').click()
    })

    it('[Step 5] should have add task button and click to open task add modal',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-button-add').should('exist').click() ;

        cy.get('.itbkk-modal-task').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-title').type('user2 first task')
        cy.get('@modal').find('.itbkk-status').select('Doing')
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
    })

    it('Should have task title "user2 first task"',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-title').contains('user2 first task').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-assignees','Unassigned')
        cy.get('@item').contains('.itbkk-status',"Doing")
    })

    it('Should have "No Description Provided","Unassigned" and "Doing".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-title').contains('user2 first task').click()
    
        cy.wait(200)
        cy.get('.itbkk-title').contains("user2 first task")
        cy.get('.itbkk-description').contains('No Description Provided').as('description')
        cy.get('.itbkk-assignees').contains('Unassigned')
        cy.get('.itbkk-status').contains('Doing')
    })

})