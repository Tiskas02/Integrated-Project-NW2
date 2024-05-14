describe(`TC-PBI7-EDIT-STATUS-2\n 
          Test Scenario : normal - edit statusName adn statusDescription \n
           -Name and Description with leading and trailing whitespace.`, () => {
  
    beforeEach(()=> {
        cy.viewport(1024, 768) ;
        cy.visit('/task') ;
        cy.wait(100) ;
    }) ;

    it('Open the task view page at /task', () => {
    })

    it('should have "Manage Status" and click to open the Status list page',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
    })

    it('Table shows Title, Description and Action', () => {
        cy.get('.itbkk-manage-status').should('exist').click() ;

        cy.wait(100) ;
        cy.contains('Name') ;
        cy.contains('Description') ;
        cy.contains('Action') ;
    })

    it('"Waiting" should have an edit button and click to open a modal to edit', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Waiting').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit','Edit').should('exist').click()
        cy.wait(100)
    })

    it('The save button should be disable and become enable when editing the name', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Waiting').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-edit','Edit').should('exist').click()
        cy.wait(100)


        cy.get('.itbkk-modal-status').as('modal')
        cy.get('@modal').find('.itbkk-status-name').clear().type('   _Dropped_   ')
        cy.get('@modal').find('.itbkk-status-description').clear().type('   _Tasks are no longer needed_   ')
        cy.get('@modal').find('.itbkk-button-confirm').click() 
        cy.wait(100)
    })

    it('The "Waiting" status is changed to "_Dropped_" status', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('_Dropped_')
    })

    it('The "TaskTitle1..." task is shown with "No Status".',()=>{
        cy.get('.itbkk-title').contains("TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0").parents(".itbkk-item").as('item')
        cy.get('@item').contains('.itbkk-title',"TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0") 
        cy.get('@item').contains('.itbkk-assignees',"Assignees1Assignees2Assignees3")
        cy.get('@item').contains('.itbkk-status',"No Status")
      })
    
      it('The "Repository" is shown with "_Dropped_" status.',()=>{
        cy.get('.itbkk-title').contains("Repository").parents(".itbkk-item").as('item')
        cy.get('@item').contains('.itbkk-title',"Repository") 
        cy.get('@item').contains('.itbkk-assignees','Unassigned').as('assignees')
        cy.get('@assignees').should('have.css','font-style','italic') 
        cy.get('@item').contains('.itbkk-status',"_Dropped_")
      })
    
      it('The "ดาต้าเบส" task is shown with "_Dropped_" status.',()=>{
        cy.get('.itbkk-title').contains("ดาต้าเบส").parents(".itbkk-item").as('item')
        cy.get('@item').contains('.itbkk-title',"ดาต้าเบส") 
        cy.get('@item').contains('.itbkk-assignees',"あなた、彼、彼女 (私ではありません)")
        cy.get('@item').contains('.itbkk-status',"_Dropped_")
      })

})