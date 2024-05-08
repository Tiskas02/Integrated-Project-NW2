describe(`TC-PBI5-EDIT-TASK-BASIC-1\n 
          Test Scenario : normal - cancel the action\n
          *****First, you should run tc-pbi3-add-task-basic-2.cy.js.`, () => {
  
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

  it('should have task "DevSecOps"',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
  })

  it('should have button action and button edit.',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-title').contains("DevSecOps").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit')
  })

  it('should click the edit button to open a mdal window with task detail',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-title').contains("DevSecOps").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit').click
  })

  it('The save button should be disable',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-title').contains("DevSecOps").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit').click()

    cy.wait(100)
    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').should('have.class','disabled')
  })

  it('The task title is deleted and click the cancel button.',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-title').contains("DevSecOps").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit').click()

    cy.wait(100)
    cy.get('.itbkk-title').clear()
    cy.get('.itbkk-button-cancel').contains('cancel',{matchCase: false}).as('cancel') ;
    cy.get('@cancel').click()

    cy.wait(100)
    cy.url().should('contain','/task')

  })

  it('should have task "DevSecOps"',()=>{
    cy.get('.itbkk-title').contains('DevSecOps').parents('.itbkk-item').as('item')
    cy.get('@item').contains('.itbkk-assignees','Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('@item').contains('.itbkk-status',"No Status")
  })

})