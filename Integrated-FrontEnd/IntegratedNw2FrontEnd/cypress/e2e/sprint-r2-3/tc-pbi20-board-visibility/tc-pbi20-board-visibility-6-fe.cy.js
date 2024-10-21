describe(`TC-PBI20-BOARD-VISIBILITY-1-FE\n 
    Test Scenario : normal
                    - authenticated user can view public board only`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.visit('/board/iZbduus70n')  // <------ add your board id here
    }) ;

    it('[Step 1] Should open the /board/:id page', () => {
    })

    it('[Step 2.1] First item should be "my frist task" with "No Status" status.',()=>{
        cy.get('.itbkk-item').eq(0).as('item')
        cy.get('@item').contains('.itbkk-title',"my first task") 
        cy.get('@item').contains('.itbkk-assignees',"Unassigned")
        cy.get('@item').contains('.itbkk-status',"No Status")
    })

    it('[Step 2.1] "add","edit","delete" and "visibility" buttons are disabled',()=>{
        cy.get('.itbkk-button-add').then( (button)=>{
            if(button.is(':disabled')){
                cy.wrap(button).should('be.disabled')
            }else{
                cy.wrap(button).should('have.class','disabled')
            }
        })
        cy.get('.itbkk-title').contains('my first task')
        cy.get('.itbkk-title').contains("my first task").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-action').then( (button)=>{
            if(button.length>0){
                cy.get('@item').find('.itbkk-button-action').click()
                cy.get('.itbkk-button-edit').then( (button)=>{
                    if(button.is(':disabled')){
                        cy.wrap(button).should('be.disabled')
                    }else{
                        cy.wrap(button).should('have.class','disabled')
                    }                    
                })
                cy.get('@item').find('.itbkk-button-action').click()
                cy.get('.itbkk-button-delete').then( (button)=>{
                    if(button.is(':disabled')){
                        cy.wrap(button).should('be.disabled')
                    }else{
                        cy.wrap(button).should('have.class','disabled')
                    }                    
                })
            }
        })

        cy.get('.itbkk-title').contains('my first task')
        cy.get('.itbkk-title').contains("my first task").parents(".itbkk-item").as("item")
        cy.get('@item').find('.itbkk-button-action').then( (button)=>{
            if(button.length>0){
                cy.get('@item').find('.itbkk-button-action').click()
                cy.get('.itbkk-button-delete').then( (button)=>{
                    if(button.is(':disabled')){
                        cy.wrap(button).should('be.disabled')
                    }else{
                        cy.wrap(button).should('have.class','disabled')
                    }                    
                })
            }
        })


        cy.get('.itbkk-board-visibility').should('be.disabled')
    })

    it('[Step 2.2] Should show "my first task" detail',()=>{

        cy.wait(100)
        cy.get('.itbkk-title').contains("my first task").click()

        cy.get('.itbkk-modal-task').should('exist').as('modal')
        cy.wait(100)

        cy.get('@modal').find('.itbkk-title').contains("my first task")
        cy.get('@modal').find('.itbkk-description').contains("No Description Provided")
        cy.get('@modal').find('.itbkk-assignees').contains("Unassigned")
        cy.get('@modal').find('.itbkk-status').contains("No Status")

    })

    it('[Step 2.3] Should show status list page with 4 statuses: No Status, In Progress, Doing and Done ',()=>{

        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100)

        cy.get('.itbkk-item').should('have.length',4) ;
        cy.get('.itbkk-status-name').contains('No Status')
        cy.get('.itbkk-status-name').contains('In Progress')
        cy.get('.itbkk-status-name').contains('Doing')
        cy.get('.itbkk-status-name').contains('Done')
    })

    it('[Step 2.4] Should show status list page with 4 statuses: No Status, In Progress, Doing and Done ',()=>{

        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100)

        cy.get('.itbkk-item').should('have.length',4) ;
        cy.get('.itbkk-status-name').contains('No Status')
        cy.get('.itbkk-status-name').contains('In Progress')
        cy.get('.itbkk-status-name').contains('Doing')
        cy.get('.itbkk-status-name').contains('Done')
    })

    it(`[Step 2.4] Should no open /board/:id/task/add page and 
                show message "Access denied, you do not have permission to view this page.`,()=>{

        cy.visit('/board/VdubmSHxvy/task/add') // <------ add your board id here

        cy.on('window:alert',(str)=>{
            expect(str).to.include('Access denied, you do not have permission to view this page.')
        })

        // cy.url().should('contain','/login')
    })    

    it(`[Step 2.5] Should no open /board/:id/task/:task-id/edit page and 
                show message "Access denied, you do not have permission to view this page.`,()=>{

        cy.visit('/board/iZbduus70n/task/84/edit') // <------ add your board id and task id here

        cy.on('window:alert',(str)=>{
            expect(str).to.include('Access denied, you do not have permission to view this page.')
        })

        // cy.url().should('contain','/login')
    })  

    it(`[Step 2.6] Should no open /board/:id/status/add page and 
                show message "Access denied, you do not have permission to view this page.`,()=>{

        cy.visit('/board/iZbduus70n/status/add') // <------ add your board id here

        cy.on('window:alert',(str)=>{
            expect(str).to.include('Access denied, you do not have permission to view this page.')
        })

        // cy.url().should('contain','/login')
    })  

    it(`[Step 2.7] Should no open /board/:id/status/:status-id/edit page and 
                show message "Access denied, you do not have permission to view this page.`,()=>{

        cy.visit('/board/iZbduus70n/status/192/edit') // <------ add your board id and status id here

        cy.on('window:alert',(str)=>{
            expect(str).to.include('Access denied, you do not have permission to view this page.')
        })

        // cy.url().should('contain','/login')
    })  

})