describe(`TC-PBI6-ADD-STATUS-1\n 
          Test Scenario : normal - add new status and cancel the operation`, () => {
  
    beforeEach(()=> {
        cy.viewport(1024, 768) ;
        cy.visit('/task') ;
        cy.wait(100) ;
    }) ;

    it('Open the task view page at /task', () => {
    })

    it('should have "Manage Status" and click to open the Status list page',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
    })

    it('Table shows Title, Description and Action', () => {
        cy.get('.itbkk-manage-status').should('exist').click() ;

        cy.wait(100) ;
        cy.contains('Name') ;
        cy.contains('Description') ;
        cy.contains('Action') ;
    })

    it('Status table shoud contain "No Status","To Do"," Doing" and "Done".',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-item').should('have.length',4) ;

        cy.get('.itbkk-item').eq(0).as('item')
        cy.get('@item').contains('.itbkk-status-name',"No Status") 
        cy.get('@item').contains('.itbkk-status-description',"The default status")

        cy.get('.itbkk-item').eq(1).as('item')
        cy.get('@item').contains('.itbkk-status-name',"To Do")

        cy.get('.itbkk-item').eq(2).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Doing")
        cy.get('@item').contains('.itbkk-status-description',"Being worked on")

        cy.get('.itbkk-item').eq(3).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Done")
        cy.get('@item').contains('.itbkk-status-description',"Finished")
    })

    it('should have an Add Status and click the button to show the modal window".',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-button-add').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-button-confirm').should('be.disabled')
        cy.get('@modal').find('.itbkk-status-name').type("Test")
        cy.get('@modal').find('.itbkk-button-confirm').should('not.be.disabled')
        cy.get('@modal').find('.itbkk-status-name').clear()
        cy.get('@modal').find('.itbkk-button-confirm').should('be.disabled')
        cy.get('@modal').find('.itbkk-button-cancel').should('exist').click()
        cy.wait(100) 

        cy.url().should('contain','/status')
    })

    it('Status table shoud contain "No Statu","To Do"," Doing" and "Done".',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-item').should('have.length',4) ;

        cy.get('.itbkk-item').eq(0).as('item')
        cy.get('@item').contains('.itbkk-status-name',"No Status") 
        cy.get('@item').contains('.itbkk-status-description',"The default status")

        cy.get('.itbkk-item').eq(1).as('item')
        cy.get('@item').contains('.itbkk-status-name',"To Do")

        cy.get('.itbkk-item').eq(2).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Doing")
        cy.get('@item').contains('.itbkk-status-description',"Being worked on")

        cy.get('.itbkk-item').eq(3).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Done")
        cy.get('@item').contains('.itbkk-status-description',"Finished")
    })
})