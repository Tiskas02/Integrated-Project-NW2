describe(`TC-PBI7-EDIT-STATUS-3\n 
          Test Scenario : normal - edit statusDescription only`, () => {
  
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

    it('"Waiting" should have an edit button and click to open a modal to edit', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Maximum001Maximum002Maximum003Maximum004Maximum005').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit','Edit').should('exist').click()
        cy.wait(100)
    })

    it('The save button should be disable and become enable when editing the name', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Maximum001Maximum002Maximum003Maximum004Maximum005').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit','Edit').should('exist').click()
        cy.wait(100)

        cy.get('.itbkk-modal-status').as('modal')
        cy.get('@modal').find('.itbkk-status-name','Maximum001Maximum002Maximum003Maximum004Maximum005')
        cy.get('@modal').find('.itbkk-status-description').clear()
        cy.get('@modal').find('.itbkk-button-confirm').click() 
        cy.wait(100)
    })

    it('The "Maximum001..." status is shown with description "No description is provided"', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Maximum001Maximum002Maximum003Maximum004Maximum005').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-status-name','Maximum001Maximum002Maximum003Maximum004Maximum005')
        cy.get('@item').contains('.itbkk-status-description','No description is provided').as('description')
        cy.get('@description').should('have.css','font-style','italic') 
    })

})