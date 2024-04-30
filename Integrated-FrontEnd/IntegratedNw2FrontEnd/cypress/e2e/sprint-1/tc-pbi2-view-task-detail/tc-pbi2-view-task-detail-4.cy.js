describe(`TC-PBI2-VIEW-TASK-TABLE-VIEW-4\n 
          Test Scenario : normal - non-English characters and 'Done' status`, () => {
  
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

  it('First item should be "_Infrastructure_"',()=>{
    cy.get('.itbkk-item').eq(3).as('item')
    cy.get('@item').contains('.itbkk-title',"_Infrastructure_") 
    cy.get('@item').contains('.itbkk-assignees',"ไก่งวง กับ เพนกวิน")
    cy.get('@item').contains('.itbkk-status',"Done")
  })

  it('Open task detail for "_Infrastructure_"',()=>{
    cy.get('.itbkk-item').contains("_Infrastructure_").click() 
  })

  it('Open task detail for "_Infrastructure_"',()=>{
    cy.get('.itbkk-item').contains("_Infrastructure_").click() 

    cy.wait(200)
    cy.get('.itbkk-title').contains("_Infrastructure_")
    cy.get('.itbkk-description').contains('_Setup containers_')
    cy.get('.itbkk-assignees').contains('ไก่งวง กับ เพนกวิน')
    cy.get('.itbkk-status').contains('Done')
    cy.get('.itbkk-timezone').contains('Asia/Bangkok')
    cy.get('.itbkk-created-on').contains('22/04/2024')
    cy.get('.itbkk-created-on').contains('16:15:00')
    cy.get('.itbkk-updated-on').contains('22/04/2024')
    cy.get('.itbkk-updated-on').contains('17:00:00')
  })

})