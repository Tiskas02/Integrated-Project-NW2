describe(`TC-PBI6-ADD-STATUS-3\n 
          Test Scenario : normal - add new status with max field size`, () => {
  
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

    it('Add the "Maximum001..." status with maximum description and click the save button.',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-button-add').click()
        cy.wait(100) ;

        cy.get('.itbkk-modal-status').should('exist').as('modal')
        cy.get('@modal').find('.itbkk-button-confirm').should('be.disabled')
        cy.get('@modal').find('.itbkk-status-name').type("Maximum001Maximum002Maximum003Maximum004Maximum005")
        cy.get('@modal').find('.itbkk-button-confirm').should('not.be.disabled')
        cy.get('@modal').find('.itbkk-status-description').type("Maximum001Maximum002Maximum003Maximum004Maximum005Maximum006Maximum007Maximum008Maximum009Maximum100Maximum001Maximum002Maximum003Maximum004Maximum005Maximum006Maximum007Maximum008Maximum009Maximum200")
        cy.get('@modal').find('.itbkk-button-confirm').should('exist').click()
        cy.wait(100) 

        cy.url().should('contain','/status')
    })

    it('The Status list page contains the "Maximum001..." with maximum description.',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;
        
        cy.get('.itbkk-status-name').contains('Maximum001Maximum002Maximum003Maximum004Maximum005').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-status-name','Maximum001Maximum002Maximum003Maximum004Maximum005')
        cy.get('@item').contains('.itbkk-status-description','Maximum001Maximum002Maximum003Maximum004Maximum005Maximum006Maximum007Maximum008Maximum009Maximum100Maximum001Maximum002Maximum003Maximum004Maximum005Maximum006Maximum007Maximum008Maximum009Maximum200').as('description')
        cy.get('@description').should('have.css','font-style','normal') 
    })

})