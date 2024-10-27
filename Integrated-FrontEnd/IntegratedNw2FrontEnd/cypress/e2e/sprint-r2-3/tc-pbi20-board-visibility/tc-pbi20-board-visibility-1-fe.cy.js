describe(`TC-PBI20-BOARD-VISIBILITY-1-FE\n 
    Test Scenario : normal
                    - owner can perform any task/status operation`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.kittiwut')
        cy.get('.itbkk-password').type('ip23/KIT')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/board')
    }) ;

    it('[Step 1] Open the login page at /login and open the /board page', () => {
    })

    it('[Step 2] Should have a "Create personal board" button and click',()=>{
        cy.get('.itbkk-button-create').should('exist').as('button')
        cy.wait(100) 

        cy.get('@button').click()
    })

    it('[Step 2] Should have a new board modal and create a personal board with default name',()=>{
        cy.get('.itbkk-button-create').should('exist').as('button')
        cy.wait(100) 

        cy.get('@button').click()
        cy.wait(100)

        cy.get('.itbkk-modal-new').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-button-ok').should('exist').click()
    })

    it('[Step 3] Should redirect to the new board and add task"my first task".',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();

        cy.get('.itbkk-button-add').should('exist').click() ;
        cy.wait(100)

        cy.get('.itbkk-modal-task').should('exist').as('modal')

        cy.get('@modal').find('.itbkk-title').type("my first task")
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100)
    })

    it('[Step 4] Should have "No Description Provided","Unassigned" and "No Status".',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();
        cy.get('.itbkk-title').contains('my first task').click()
        cy.wait(200)

        cy.get('.itbkk-title').contains("my first task")
        cy.get('.itbkk-description').contains('No Description Provided').as('description')
        cy.get('@description').should('have.css','font-style','italic') 
        cy.get('.itbkk-assignees').contains('Unassigned').as('assignees')
        cy.get('@assignees').should('have.css','font-style','italic') 
        cy.get('.itbkk-status').contains('No Status')
      })

      it('[Step 5] Should change status of "my first task" to "To Do".',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();
        cy.get('.itbkk-title').contains('my first task')
        cy.get('.itbkk-title').contains("my first task").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-action').click().as('action')
        cy.get('@action').find('.itbkk-button-edit').click()
        cy.wait(200)

        cy.get('.itbkk-modal-task').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-status').select('To Do')
        cy.get('@modal').find('.itbkk-button-confirm').click()

        cy.wait(100)
        cy.url().should('contain','/board')
      })

      it('[Step 6] Should delete "my first task" and should not have "my first task".',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();
        cy.get('.itbkk-title').contains('my first task')
        cy.get('.itbkk-title').contains("my first task").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-status').contains('To Do')
        cy.get('@item').find('.itbkk-button-action').click().as('action')
        cy.get('@action').find('.itbkk-button-delete').click()
        cy.wait(200)

        cy.get('.itbkk-message').contains('Do you want to delete the task number')
        cy.get('.itbkk-message').contains('my first task')
        cy.get('.itbkk-button-confirm').click()

        cy.wait(100)
        cy.url().should('contain','/board')
        cy.should('not.contain','my first task')
      })

      it('[Step 7] Should have "Manage Status" and click to open the Status list page',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) 

        cy.get('.itbkk-status-name').contains("To Review").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-delete').click()
        cy.wait(100) ;

        cy.get('.itbkk-message').contains('Do you want to delete the To Review status')
        cy.get('.itbkk-button-confirm').click()

        cy.get('.itbkk-item').should('have.length',4) ;
        cy.get('.itbkk-item').eq(0).as('item')
        cy.get('@item').contains('.itbkk-status-name',"No Status")

        cy.get('.itbkk-item').eq(1).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Done")

        cy.get('.itbkk-item').eq(2).as('item')
        cy.get('@item').contains('.itbkk-status-name',"To Do")

        cy.get('.itbkk-item').eq(3).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Doing")
    })

    it('[Step 8] Should add the "To Review" status with no description and click the save button.',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-button-add').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-status-name').type("To Review")
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100) 

        cy.url().should('contain','/status')
        cy.wait(100)

        cy.get('.itbkk-status-name').contains('To Review')
    })

    it('[Step 9] Should change the "To Do" status to "In Progress" and click the save button.',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains("To Do").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-edit').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-status-name').clear()
        cy.get('@modal').find('.itbkk-status-name').type("In Progress")
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100) 

        cy.url().should('contain','/status')
        cy.wait(100)

        cy.get('.itbkk-status-name').contains('In Progress')
    })

    it('[Step 10] Should delete the "To Review" status and should not in the the status page.',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains("To Review").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-delete').click()
        cy.wait(100) ;

        cy.get('.itbkk-message').contains('Do you want to delete the To Review status')
        cy.get('.itbkk-button-confirm').click()

        cy.url().should('contain','/status')
        cy.wait(100)

        cy.get('.itbkk-status-name').contains('In Progress')
    })

    it('[Step 11] Should redirect to the new board and add task"my first task".',()=>{
        cy.contains('ITBKK KITTIWUT Personal Board')

        cy.get('.itbkk-button-add').should('exist').click() ;
        cy.wait(100)

        cy.get('.itbkk-modal-task').should('exist').as('modal')

        cy.get('@modal').find('.itbkk-title').type("my first task")
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100)
    })
})