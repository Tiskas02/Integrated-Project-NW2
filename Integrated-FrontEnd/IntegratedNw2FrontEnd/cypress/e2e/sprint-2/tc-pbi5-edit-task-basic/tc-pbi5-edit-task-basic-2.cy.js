describe(`TC-PBI5-EDIT-TASK-BASIC-2\n 
          Test Scenario : normal - all fields\n
          *****First, you should run tc-pbi3-add-task-basic-2.cy.js.`, () => {

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

  it('Change task title, description and assignees to "DevSecOps2" and click the save button.',()=>{
    cy.get('.itbkk-title').contains("DevSecOps")
    cy.get('.itbkk-title').contains("DevSecOps").parents(".itbkk-item").as("item")
    cy.get('@item').find('.itbkk-button-action').click().as('action')
    cy.get('@action').find('.itbkk-button-edit').click()

    cy.wait(100)
    cy.get('.itbkk-title').clear().type("DevSecOps2")
    cy.wait(100)
    cy.get('.itbkk-description').clear().type("DevSecOps2")
    cy.get('.itbkk-assignees').clear().type("DevSecOps2")
    cy.get('.itbkk-status').select('Doing')
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

  it('should have task "DevSecOps2" and Doing',()=>{
    cy.get('.itbkk-title').contains('DevSecOps2').parents('.itbkk-item').as('item')
    cy.get('@item').contains('.itbkk-assignees','DevSecOps2').as('assignees')
    cy.get('@item').contains('.itbkk-status',"Doing")
  })

  it('Task details show all fields as "DevSecOps2, status si Doing, createdOn is unchanged, and updatedOn has been changed to the current time.',()=>{
    cy.get('.itbkk-title').contains('DevSecOps').click()

    cy.wait(200)
    cy.get('.itbkk-title').contains("DevSecOps2")
    cy.get('.itbkk-description').contains('DevSecOps2')
    cy.get('.itbkk-assignees').contains('DevSecOps2')
    cy.get('.itbkk-status').contains('Doing')
    cy.get('.itbkk-created-on').contains(prevCreatedOn)
    cy.get('.itbkk-updated-on').should('not.equal',prevUpdatedOn)
  })

})