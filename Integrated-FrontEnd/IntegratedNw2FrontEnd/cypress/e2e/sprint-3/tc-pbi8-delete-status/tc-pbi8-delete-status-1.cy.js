describe(`TC-PBI8-DELETE-STATUS-1\n 
          Test Scenario : normal - cannot delete No Status, cancel delete status`, () => {
  
    beforeEach(()=> {
        cy.viewport(1024, 768)
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

    it('"No Status" should not have any button to delete', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('No Status').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-delete').should('not.exist')
    })

    it('The "Max.." status should have a button to delete and click the delete button and press the cancel', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Maximum001Maximum002Maximum003Maximum004Maximum005').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-delete','Delete').click()

        cy.get('.itbkk-message').contains('Do you want to delete ')
        cy.get('.itbkk-message').contains('Maximum001Maximum002Maximum003Maximum004Maximum005')
        cy.get('.itbkk-button-confirm').contains('Confirm')
        cy.get('.itbkk-button-cancel').contains('Cancel').click()
        cy.wait(100)

        cy.url().should('contain','/status')
    })
})