describe(`TC-PBI3-ADD-TASK-BASIC-3\n 
          Test Scenario : normal - all fields, max size,To Do`, () => {
  
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

    cy.get('.itbkk-title').type("DevSecOps1DevSecOps2DevSecOps3DevSecOps4DevSecOps5DevSecOps6DevSecOps7DevSecOps8DevSecOps9DevSecOps0")
    cy.get('.itbkk-description').type("Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5")
    cy.get('.itbkk-assignees').type("Assignees1Assignees2Assignees3")
    cy.get('.itbkk-status').select('To Do')
    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').click()
  })

  it('Should have task title "DevSecOps1DevSecOps2DevSecOps3...."',()=>{
    cy.get('.itbkk-title').contains('DevSecOps1DevSecOps2DevSecOps3DevSecOps4DevSecOps5DevSecOps6DevSecOps7DevSecOps8DevSecOps9DevSecOps0').parents('.itbkk-item').as('item')
    cy.get('@item').contains('.itbkk-assignees','Assignees1Assignees2Assignees3')
    cy.get('@item').contains('.itbkk-status',"To Do")
  })

  it('Should have description, assignees and "To Do".',()=>{
    cy.get('.itbkk-title').contains('DevSecOps1DevSecOps2DevSecOps3DevSecOps4DevSecOps5DevSecOps6DevSecOps7DevSecOps8DevSecOps9DevSecOps0').click()

    cy.wait(200)
    cy.get('.itbkk-title').contains("DevSecOps1DevSecOps2DevSecOps3DevSecOps4DevSecOps5DevSecOps6DevSecOps7DevSecOps8DevSecOps9DevSecOps0")
    cy.get('.itbkk-description').contains('Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5')
    cy.get('.itbkk-assignees').contains('Assignees1Assignees2Assignees3')
    cy.get('.itbkk-status').contains('To Do')
  })

})