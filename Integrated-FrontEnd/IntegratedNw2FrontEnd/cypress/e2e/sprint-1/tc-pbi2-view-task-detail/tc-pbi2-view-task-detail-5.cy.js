describe(`TC-PBI2-VIEW-TASK-TABLE-VIEW-5\n 
          Test Scenario : fail - view task with invalid ID`, () => {
  
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

  it('Task table has 4 task entries.',()=>{
    cy.get('.itbkk-item').should('have.length',4) ;
  })

  it('Open tsak view page at /task/100',()=>{
    cy.visit('/task/100')
    cy.wait(100)

    cy.on('window:alert',(str)=>{
      expect(str).to.equal('The requested task does not exist')
    })
    cy.location().should((location)=>{
        expect(location.pathname).to.eq('/task')
    })
  })
})