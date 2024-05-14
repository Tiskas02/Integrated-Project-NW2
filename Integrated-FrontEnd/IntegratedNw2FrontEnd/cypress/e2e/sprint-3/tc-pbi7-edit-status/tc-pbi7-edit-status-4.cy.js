describe(`TC-PBI7-EDIT-STATUS-4\n 
          Test Scenario : failed - status (id) does not exist`, () => {
  
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

    it('should show "An error has occurred, the status does not exist" when typing route "/status/1000/edit".', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.visit('/status/1000/edit')
        cy.wait(100) ;
        cy.get('.itbkk-message',"An error has occurred, the status does not exist") 
        cy.wait(100)

        cy.url().should('contain','/status')
    })

})