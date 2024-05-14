describe(`TC-PBI6-ADD-STATUS-5\n 
          Test Scenario : normal - add 3 tasks with No Status and To Do status`, () => {
  
  beforeEach(()=> {
    cy.viewport(1024, 768) ;
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

  it('Add the "TaskTitle..." with "No Status" status.',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
    cy.wait(100)

    cy.get('.itbkk-modal-task').should('exist').as('modal')
    cy.get('@modal').find('.itbkk-title').type("TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0")
    cy.get('@modal').find('.itbkk-description').type("Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5")
    cy.get('@modal').find('.itbkk-assignees').type('Assignees1Assignees2Assignees3')
    cy.get('@modal').find('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').click()
  })

  it('Add the "Repository" with "To Do" status.',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
    cy.wait(100)

    cy.get('.itbkk-modal-task').should('exist').as('modal')
    cy.get('@modal').find('.itbkk-title').type("Repository")
    cy.get('@modal').find('.itbkk-status').select('Waiting')
    cy.get('@modal').find('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').click()
  })

  it('Add the "ดาต้าเบส" with "Waiting" status.',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
    cy.wait(100)

    cy.get('.itbkk-modal-task').should('exist').as('modal')
    cy.get('@modal').find('.itbkk-title').type("ดาต้าเบส")
    cy.get('@modal').find('.itbkk-description').type("ສ້າງຖານຂໍ້ມູນ")
    cy.get('@modal').find('.itbkk-.assignees').type('あなた、彼、彼女 (私ではありません)')
    cy.get('@modal').find('.itbkk-status').select('Waiting')
    cy.get('@modal').find('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').click()
  })

  it('The "TaskTitle1..." task is shown with "No Status".',()=>{
    cy.get('.itbkk-title').contains("TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0").parents(".itbkk-item").as('item')
    cy.get('@item').contains('.itbkk-title',"TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0") 
    cy.get('@item').contains('.itbkk-assignees',"Assignees1Assignees2Assignees3")
    cy.get('@item').contains('.itbkk-status',"No Status")
  })

  it('The "Repository" is shown with "Waiting" status.',()=>{
    cy.get('.itbkk-title').contains("Repository").parents(".itbkk-item").as('item')
    cy.get('@item').contains('.itbkk-title',"Repository") 
    cy.get('@item').contains('.itbkk-assignees','Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('@item').contains('.itbkk-status',"Waiting")
  })

  it('The "ดาต้าเบส" task is shown with "Waiting" status.',()=>{
    cy.get('.itbkk-title').contains("ดาต้าเบส").parents(".itbkk-item").as('item')
    cy.get('@item').contains('.itbkk-title',"ดาต้าเบส") 
    cy.get('@item').contains('.itbkk-assignees',"あなた、彼、彼女 (私ではありません)")
    cy.get('@item').contains('.itbkk-status',"Waiting")
  })

})