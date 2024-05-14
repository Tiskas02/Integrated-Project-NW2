describe(`TC-PBI8-DELETE-STATUS-4\n 
          Test Scenario : fail - try to delete the same status twice on different browsers`, () => {
  
    beforeEach(()=> {
        cy.viewport(1024, 768)
        cy.visit('/task') ;
        cy.wait(100) ;
    }) ;
    
    it('Run this test with your advisor.', () => {
    
    })
})