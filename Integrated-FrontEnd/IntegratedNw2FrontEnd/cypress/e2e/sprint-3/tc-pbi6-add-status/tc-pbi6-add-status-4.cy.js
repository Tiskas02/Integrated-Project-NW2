describe(`TC-PBI6-ADD-STATUS-4\n 
          Test Scenario : normal - all all fields with leading and trailling whitespace`, () => {
  
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

    it('Add the "     _trim_     " status with "     _trim_     " description  and click the save button.',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-button-add').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-button-confirm').should('be.disabled')
        cy.get('@modal').find('.itbkk-status-name').type("     _trim_     ")
        cy.get('@modal').find('.itbkk-button-confirm').should('not.be.disabled')
        cy.get('@modal').find('.itbkk-status-description').type("     _trim_     ")
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100) 

        cy.url().should('contain','/status')
    })

    it('The Status list page contains the "_trim_" status with "_trim_" description.',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;
        
        cy.get('.itbkk-status-name').contains('_trim_').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-status-name','_trim_')
        cy.get('@item').contains('.itbkk-status-description','_trim_').as('description')
        cy.get('@description').should('have.css','font-style','normal') 
    })

})