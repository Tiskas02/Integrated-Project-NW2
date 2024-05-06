describe(`TC-PBI5-EDIT-TASK-BASIC-3\n 
          Test Scenario : normal - optional fields\n
          *****First, you should run tc-pbi5-edit-task-basic-2.cy.js.`, () => {

  let prevCreatedOn
  let prevUpdatedOn
  
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

  it('should have task "DevSecOps2"',()=>{
    cy.get('.itbkk-title').contains("DevSecOps2")
  })

  it('should have button action and button edit.',()=>{
    cy.get('.itbkk-title').contains("DevSecOps2")
    cy.get('.itbkk-title').contains("DevSecOps2").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit')
  })

  it('should click the edit button to open a mdal window with task detail',()=>{
    cy.get('.itbkk-title').contains("DevSecOps2")
    cy.get('.itbkk-title').contains("DevSecOps2").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit').click
  })

  it('The save button should be disable',()=>{
    cy.get('.itbkk-title').contains("DevSecOps2")
    cy.get('.itbkk-title').contains("DevSecOps2").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit').click()

    cy.wait(100)
    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').should('have.class','disabled')
  })

  it('Delete description and assignees fields, change status to "No Status" and click the save button.',()=>{
    cy.get('.itbkk-title').contains("DevSecOps2")
    cy.get('.itbkk-title').contains("DevSecOps2").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit').click()

    cy.wait(100)
    cy.get('.itbkk-description').clear()
    cy.get('.itbkk-assignees').clear()
    cy.get('.itbkk-status').select('No Status')
    cy.get('.itbkk-created-on').then((createdOn)=>{
        prevCreatedOn = createdOn.text()
    })
    cy.get('.itbkk-updated-on').then((updatedOn)=>{
        prevUpdatedOn = updatedOn.text()
    })
    cy.get('.itbkk-button-confirm').contains('save',{matchCase: false}).as('save') ;
    cy.get('@save').click()

    cy.wait(100)
    cy.url().should('contain','/task')

  })

  it('should have task "DevSecOps2" with "No Description Provided", "Unassigned" and "No Status".',()=>{
    cy.get('.itbkk-title').contains('DevSecOps2').parents('.itbkk-item').as('item')
    cy.get('@item').contains('.itbkk-assignees','Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('@item').contains('.itbkk-status',"No Status")
  })

  it('Task details show all fields as "DevSecOps2, status si Doing, createdOn is unchanged, and updatedOn has been changed to the current time.',()=>{
    cy.get('.itbkk-title').contains('DevSecOps2').click()

    cy.wait(200)
    cy.get('.itbkk-title').contains("DevSecOps2")
    cy.get('.itbkk-description').contains('No Description Provided').as('description')
    cy.get('@description').should('have.css','font-style','italic') 
    cy.get('.itbkk-assignees').contains('Unassigned').as('assignees')
    cy.get('@assignees').should('have.css','font-style','italic') 
    cy.get('.itbkk-status').contains('No Status')
    cy.get('.itbkk-created-on').contains(prevCreatedOn)
    cy.get('.itbkk-updated-on').should('not.equal',prevUpdatedOn)
  })

})