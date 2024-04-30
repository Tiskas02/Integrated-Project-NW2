describe(`TC-PBI2-VIEW-TASK-TABLE-VIEW-2\n 
          Test Scenario : normal - empty Description and Assignees and 'To Do' status`, () => {
  
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

  it('First item should be "Repository"',()=>{
    cy.get('.itbkk-item').eq(1).as('item')
    cy.get('@item').contains('.itbkk-title',"Repository") 
    cy.get('@item').contains('.itbkk-assignees','Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('@item').contains('.itbkk-status',"To Do")
  })

  it('Open task detail for "Repository"',()=>{
    cy.get('.itbkk-title').contains("Repository").click() 
  })

  it('Open task detail for "Repository"',()=>{
    cy.get('.itbkk-title').contains("Repository").click() 

    cy.wait(200)
    cy.get('.itbkk-title').contains("Repository")
    cy.get('.itbkk-description').contains('No Description Provided').as('description')
    cy.get('@description').should('have.css','font-style','italic') 
    cy.get('.itbkk-assignees').contains('Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('.itbkk-status').contains('To Do')
    cy.get('.itbkk-timezone').contains('Asia/Bangkok')
    cy.get('.itbkk-created-on').contains('22/04/2024')
    cy.get('.itbkk-created-on').contains('16:05:00')
    cy.get('.itbkk-updated-on').contains('22/04/2024')
    cy.get('.itbkk-updated-on').contains('21:00:00')
  })

})