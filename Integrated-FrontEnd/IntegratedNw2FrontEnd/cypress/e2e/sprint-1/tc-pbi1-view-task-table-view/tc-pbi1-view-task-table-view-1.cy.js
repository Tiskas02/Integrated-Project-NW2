describe(`TC-PBI1-VIEW-TASK-TABLE-VIEW-1\n 
          Test Scenario : normal - task table is empty`, () => {
  
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

  it('Task table shoud be empty.',()=>{
    cy.get('.itbkk-item').should('have.length',0) ;
  })
})