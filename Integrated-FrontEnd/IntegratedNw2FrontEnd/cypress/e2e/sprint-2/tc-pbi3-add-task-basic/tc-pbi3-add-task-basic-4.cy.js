describe(`TC-PBI3-ADD-TASK-BASIC-4\n 
          Test Scenario : normal - trimmed, Done`, () => {
  
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

    cy.get('.itbkk-title').type("     _Front End_     ")
    cy.wait(100)
    cy.get('.itbkk-description').type("     _fix CSS on landing page_     ")
    cy.get('.itbkk-assignees').type("     _ปุยฝ้าย กับ เสือดาว_     ")
    cy.get('.itbkk-status').select('Done')
    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').click()
  })

  it('Should have task title "_Front End_"',()=>{
    cy.get('.itbkk-title').contains('_Front End_').parents('.itbkk-item').as('item')
    cy.get('@item').contains('.itbkk-assignees','_ปุยฝ้าย กับ เสือดาว_')
    cy.get('@item').contains('.itbkk-status',"Done")
  })

  it('Should trim title, description, assignees and "Done".',()=>{
    cy.get('.itbkk-title').contains('_Front End_').click()

    cy.wait(200)
    cy.get('.itbkk-title').contains("_Front End_")
    cy.get('.itbkk-description').contains('_fix CSS on landing page_')
    cy.get('.itbkk-assignees').contains('_ปุยฝ้าย กับ เสือดาว_')
    cy.get('.itbkk-status').contains('Done')
  })

})