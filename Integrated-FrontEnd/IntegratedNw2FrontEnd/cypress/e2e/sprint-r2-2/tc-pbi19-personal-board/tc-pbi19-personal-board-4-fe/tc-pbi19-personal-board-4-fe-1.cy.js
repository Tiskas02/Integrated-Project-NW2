describe(`TC-PBI19-PERSIONAL-BOARD-4-FE-1\n 
    Test Scenario : normal
                    - custom status is specific to each board
                    - basic status CRUD
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

    it('[Step 1.1] should have "Manage Status" and click to open the Status list page',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
    })

    it('[Step 1.1] Status table shoud contain "No Status","To Do"," Doing" and "Done".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

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

    it('[Step 1.2] should have an Add Status and click the button to show the modal window and add status "To Review".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-button-add').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-status-name').type("To Review")
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100) 

        cy.url().should('contain','/status')

        cy.get('.itbkk-item').should('have.length',5) ;
        cy.get('.itbkk-status-name').contains('To Review')
    })

    it('[Step 1.3] should have add task button and click to open task add modal and add "user1 first task" with "To Review".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-button-add').should('exist').click() ;

        cy.get('.itbkk-modal-task').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-title').type('user1 first task')
        cy.get('@modal').find('.itbkk-status').select('To Review')
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
    })

    it('[Step 1.3] Should have task title "user1 first task" with "To Review".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-assignees','Unassigned')
        cy.get('@item').contains('.itbkk-status',"To Review")
    })

    it('[Step 1.4] should have add task button and click to open task add modal and add "user1 second task" with "Doing".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-button-add').should('exist').click() ;

        cy.get('.itbkk-modal-task').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-title').type('user1 second task')
        cy.get('@modal').find('.itbkk-status').select('Doing')
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
    })

    it('[Step 1.4] Should have task title "user1 second task" with "Doing".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-title').contains('user1 second task').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-assignees','Unassigned')
        cy.get('@item').contains('.itbkk-status',"Doing")
    })

    it('[Step 1.5] Should change "Doing" status to "In Progress" status.', ()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Doing').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit','Edit').should('exist').click()
        cy.wait(100)

        cy.get('.itbkk-modal-status').as('modal')
        cy.get('@modal').find('.itbkk-status-name','Doing')
        cy.get('@modal').find('.itbkk-status-name').clear()
        cy.get('@modal').find('.itbkk-status-name').type('In Progress')
        cy.get('@modal').find('.itbkk-button-confirm').click() 
        cy.wait(100)
    })

    it('[Step 1.5] should have task title "user1 second task" with "In Progress".',()=>{
        cy.get('.itbkk-title').contains('user1 first task').parents('.itbkk-item').as('item').click();
        cy.get('.itbkk-status').contains('In Progress').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-title','user1 second task')
        cy.get('@item').contains('.itbkk-assignees','Unassigned')
        cy.get('@item').contains('.itbkk-status',"In Progress")
    })
})