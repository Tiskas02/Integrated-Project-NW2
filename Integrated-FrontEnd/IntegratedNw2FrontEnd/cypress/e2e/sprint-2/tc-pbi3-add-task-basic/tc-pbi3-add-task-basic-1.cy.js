describe(`TC-PBI3-ADD-TASK-BASIC-1\n 
          Test Scenario : normal - taskTitle is not entered, cancel the action`, () => {
  
  beforeEach(()=> {
    cy.visit('/task') ;
    cy.wait(100) ;
  }) ;

  it('Open the task view page at /task', () => {
  })

  it('Table shows Title, Assignees and Status', () => {
    cy.contains('Title') ;
    cy.contains('Assignees') ;
    cy.contains('Status') ;
  })

  it('Task table shoud not be empty.',()=>{
    cy.get('.itbkk-item').should('have.length.greaterThan',0) ;
  })

  it('should have add button and click to open modal',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
  })

  it('should have save button ',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
    cy.wait(100)

    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').should('have.class','disabled')
    cy.get('.itbkk-button-cancel').contains('cancel',{matchCase: false}).click()
  })

  it('should return the task list page when clicking the Cancel button',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
    cy.wait(100)

    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').should('have.class','disabled')
    cy.get('.itbkk-button-cancel').contains('cancel',{matchCase: false}).click()
    cy.url().should('contain','/task')
  })
})