describe(`TC-PBI3-ADD-TASK-BASIC-2\n 
          Test Scenario : normal - optional fields, default status`, () => {
  
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

  it('should have add button and click to open modal',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
  })

  it('should have save button ',()=>{
    cy.get('.itbkk-button-add').should('exist').click() ;
    cy.wait(100)

    cy.get('.itbkk-title').type("DevSecOps")
    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').click()
  })

  it('Should have task title "DevSecOps"',()=>{
    cy.get('.itbkk-title').contains('DevSecOps').parents('.itbkk-item').as('item')
    cy.get('@item').contains('.itbkk-assignees','Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('@item').contains('.itbkk-status',"No Status")
  })

  it('Should have "No Description Provided","Unassigned" and "No Status".',()=>{
    cy.get('.itbkk-title').contains('DevSecOps').click()

    cy.wait(200)
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-description').contains('No Description Provided').as('description')
    cy.get('@description').should('have.css','font-style','italic') 
    cy.get('.itbkk-assignees').contains('Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('.itbkk-status').contains('No Status')
  })

})