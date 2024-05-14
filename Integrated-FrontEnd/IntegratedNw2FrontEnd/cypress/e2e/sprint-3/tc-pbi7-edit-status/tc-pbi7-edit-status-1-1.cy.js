describe(`TC-PBI7-EDIT-STATUS-1-1\n 
          Test Scenario : normal - cannot edit 'No Status', enable Save on edit, cancel edit`, () => {
  
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

    it('"No Status" should not have any button to edit or delete', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('No Status').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit').should('not.exist')
        cy.get('@item').contains('.itbkk-button-delete').should('not.exist')
    })

    it('The route "/status/1/edit" should returns to the status list page', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.visit('/status/1/edit') ;
        cy.wait(100) ;

        cy.url().should('contain','/status')
    })

})