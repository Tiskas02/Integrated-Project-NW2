describe(`TC-PBI2-VIEW-TASK-TABLE-VIEW-1\n 
          Test Scenario : normal - task detail of row 1`, () => {
  
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

  it('First item should be "TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0"',()=>{
    cy.get('.itbkk-item').eq(0).as('item')
    cy.get('@item').contains('.itbkk-title',"TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0") 
    cy.get('@item').contains('.itbkk-assignees',"Assignees1Assignees2Assignees3")
    cy.get('@item').contains('.itbkk-status',"No Status")
  })

  it('Open task detail for "TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0"',()=>{
    cy.get('.itbkk-item').contains('.itbkk-title',"TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0").click() 
  })

  it('Open task detail for "TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0"',()=>{
    cy.get('.itbkk-item').contains('.itbkk-title',"TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0").click() 

    cy.wait(200)
    cy.get('.itbkk-title').contains("TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0")
    cy.get('.itbkk-description').contains("Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5")
    cy.get('.itbkk-status').contains('No Status')
    cy.get('.itbkk-timezone').contains('Asia/Bangkok')
    cy.get('.itbkk-created-on').contains('22/04/2024')
    cy.get('.itbkk-created-on').contains('16:00:00')
    cy.get('.itbkk-updated-on').contains('22/04/2024')
    cy.get('.itbkk-updated-on').contains('16:00:00')
  })

})