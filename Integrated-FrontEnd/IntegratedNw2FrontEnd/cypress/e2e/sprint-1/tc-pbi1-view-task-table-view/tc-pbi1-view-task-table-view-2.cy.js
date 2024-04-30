describe(`TC-PBI1-VIEW-TASK-TABLE-VIEW-2\n 
          Test Scenario : normal - task table has entries`, () => {
  
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

  it('Second item should be "Repository"',()=>{
    cy.get('.itbkk-item').eq(1).as('item')
    cy.get('@item').contains('.itbkk-title',"Repository") 
    cy.get('@item').contains('.itbkk-assignees','Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('@item').contains('.itbkk-status',"To Do")
  })

  it('Third item should be "ดาต้าเบส"',()=>{
    cy.get('.itbkk-item').eq(2).as('item')
    cy.get('@item').contains('.itbkk-title',"ดาต้าเบส") 
    cy.get('@item').contains('.itbkk-assignees',"あなた、彼、彼女 (私ではありません)")
    cy.get('@item').contains('.itbkk-status',"Doing")
  })

  it('Forth item should be "_Infrastructure_"',()=>{
    cy.get('.itbkk-item').eq(3).as('item')
    cy.get('@item').contains('.itbkk-title',"_Infrastructure_") 
    cy.get('@item').contains('.itbkk-assignees',"ไก่งวง กับ เพนกวิน")
    cy.get('@item').contains('.itbkk-status',"Done")
  })
})