describe(`TC-PBI2-VIEW-TASK-TABLE-VIEW-3\n 
          Test Scenario : normal - non-English characters and 'Doing' status`, () => {
  
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

  it('First item should be "ดาต้าเบส"',()=>{
    cy.get('.itbkk-item').eq(2).as('item')
    cy.get('@item').contains('.itbkk-title',"ดาต้าเบส") 
    cy.get('@item').contains('.itbkk-assignees',"あなた、彼、彼女 (私ではありません)")
    cy.get('@item').contains('.itbkk-status',"Doing")
  })

  it('Open task detail for "ดาต้าเบส"',()=>{
    cy.get('.itbkk-item').contains("ดาต้าเบส").click() 
  })

  it('Open task detail for "ดาต้าเบส"',()=>{
    cy.get('.itbkk-item').contains("ดาต้าเบส").click() 

    cy.wait(200)
    cy.get('.itbkk-title').contains("ดาต้าเบส")
    cy.get('.itbkk-description').contains('ສ້າງຖານຂໍ້ມູນ')
    cy.get('.itbkk-assignees').contains('あなた、彼、彼女 (私ではありません)')
    cy.get('.itbkk-status').contains('Doing')
    cy.get('.itbkk-timezone').contains('Asia/Bangkok')
    cy.get('.itbkk-created-on').contains('22/04/2024')
    cy.get('.itbkk-created-on').contains('16:10:00')
    cy.get('.itbkk-updated-on').contains('25/04/2024')
    cy.get('.itbkk-updated-on').contains('07:00:00')
  })

})