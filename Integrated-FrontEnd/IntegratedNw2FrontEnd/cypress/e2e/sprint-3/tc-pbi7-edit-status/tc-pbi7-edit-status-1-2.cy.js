describe(`TC-PBI7-EDIT-STATUS-1-2\n 
          Test Scenario : normal - edit "To Do", save should disabled and does not changed when canceling`, () => {
  
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

    it('"To Do" should have an edit button and click to open a modal to edit', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('To Do').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit','Edit').should('exist').click()
        cy.wait(100)
    })

    it('The save button should be disable and become enable when editing the name', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('To Do').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit','Edit').should('exist').click()
        cy.wait(100)


        cy.get('.itbkk-modal-status').as('modal')
        cy.get('@modal').find('.itbkk-status-name','To Do')
        cy.get('@modal').find('.itbkk-status-description','')
        cy.get('@modal').find('.itbkk-button-confirm').as('save')

        cy.get('@save').should('be.disabled')

        cy.wait(100)
        cy.get('@modal').find('.itbkk-status-name').clear().type('To Do New')
        cy.get('@save').should('not.be.disabled')
        cy.get('@modal').find('.itbkk-button-cancel').click() 
        cy.wait(100)
    })

    it('The "To Do" status should not be changed', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('To Do')
    })

})