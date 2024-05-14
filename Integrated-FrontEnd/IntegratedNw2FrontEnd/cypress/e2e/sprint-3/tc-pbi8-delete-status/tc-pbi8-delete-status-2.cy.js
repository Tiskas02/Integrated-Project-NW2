describe(`TC-PBI8-DELETE-STATUS-2\n 
          Test Scenario : normal - delete status with no task`, () => {
  
    beforeEach(()=> {
        cy.viewport(1024, 768)
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

    it('The "Max.." status should have a button to delete and click the delet button and press the confirm.', ()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-status-name').contains('Maximum001Maximum002Maximum003Maximum004Maximum005').parents('.itbkk-item').as('item')
        cy.get('@item').contains('.itbkk-button-delete','Delete').click()

        cy.get('.itbkk-message').contains('Do you want to delete ')
        cy.get('.itbkk-message').contains('Maximum001Maximum002Maximum003Maximum004Maximum005')
        cy.get('.itbkk-button-cancel').contains('Cancel')
        cy.get('.itbkk-button-confirm').contains('Confirm').click()
        cy.wait(100)

        cy.url().should('contain','/status')
    })

    it('Status table shoud contain "No Status","To Do"," Doing", "Done", "_Dropped_" and "_Trim_".',()=>{
        cy.get('.itbkk-manage-status').should('exist').click() ;
        cy.wait(100) ;

        cy.get('.itbkk-item').should('have.length',6) ;

        cy.get('.itbkk-item').eq(0).as('item')
        cy.get('@item').contains('.itbkk-status-name',"No Status") 
        cy.get('@item').contains('.itbkk-status-description',"The default status")

        cy.get('.itbkk-item').eq(1).as('item')
        cy.get('@item').contains('.itbkk-status-name',"To Do")

        cy.get('.itbkk-item').eq(2).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Doing")
        cy.get('@item').contains('.itbkk-status-description',"Being worked on")

        cy.get('.itbkk-item').eq(3).as('item')
        cy.get('@item').contains('.itbkk-status-name',"Done")
        cy.get('@item').contains('.itbkk-status-description',"Finished"),

        cy.get('.itbkk-item').eq(4).as('item')
        cy.get('@item').contains('.itbkk-status-name',"_Dropped_")
        cy.get('@item').contains('.itbkk-status-description',"_Tasks are no longer needed_")

        cy.get('.itbkk-item').eq(5).as('item')
        cy.get('@item').contains('.itbkk-status-name',"_trim_")
        cy.get('@item').contains('.itbkk-status-description',"_trim_")
    })
})