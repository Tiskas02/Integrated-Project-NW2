describe(`TC-PBI6-ADD-STATUS-2\n 
          Test Scenario : normal - add new status with no description`, () => {
  
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

    it('should have an Add Status and click the button to show the modal window".',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-button-add').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist')
    })

    it('Add the "Waiting" status with no description and click the save button.',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-button-add').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-button-confirm').should('be.disabled')
        cy.get('@modal').find('.itbkk-status-name').type("Waiting")
        cy.get('@modal').find('.itbkk-button-confirm').should('not.be.disabled')
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100) 

        cy.url().should('contain','/status')
    })

    it('The Status list page contains the "Waiting" with "No description is provided".',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;
        
        cy.get('.itbkk-status-name').contains('Waiting').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-status-name','Waiting')
        cy.get('@item').contains('.itbkk-status-description','No description is provided').as('description')
        cy.get('@description').should('have.css','font-style','italic') 
    })

})