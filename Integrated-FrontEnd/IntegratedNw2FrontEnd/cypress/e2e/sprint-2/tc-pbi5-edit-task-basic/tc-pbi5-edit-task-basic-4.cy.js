describe(`TC-PBI5-EDIT-TASK-BASIC-1\n 
          Test Scenario : failed - task (id) does not exit`, () => {
  
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

  it('Open task edit page for id 100.',()=>{
    cy.visit('/task/100/edit')

    cy.on('window:alert',(str)=>{
        expect(str).to.equal('The requested task does not exist')
    })
    cy.wait(100)

    cy.location().should((location)=>{
        expect(location.pathname).to.eq('/task')
    })

  })

})