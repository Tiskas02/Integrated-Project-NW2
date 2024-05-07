describe(`TC-PBI4-DELETE-TASK-BASIC-1\n 
          Test Scenario : normal - cancel the action`, () => {
  
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

  it('should have button action and button delete, then click the delete button',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-title').contains("DevSecOps").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-delete').click()
    cy.get('.itbkk-message').contains('Do you want to delete the task number')
    cy.get('.itbkk-message').contains('DevSecOps')
  })

  it('should have confirm button and click',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-title').contains("DevSecOps").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-delete').click()
    cy.get('.itbkk-message').contains('Do you want to delete the task number')
    cy.get('.itbkk-message').contains('DevSecOps')
    cy.get('.itbkk-button-cancel').contains("Cancel").click()

    cy.wait(100)
    cy.url().should('contain','/task')
  })

  it('should have task "DevSecOps"',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
  })

})