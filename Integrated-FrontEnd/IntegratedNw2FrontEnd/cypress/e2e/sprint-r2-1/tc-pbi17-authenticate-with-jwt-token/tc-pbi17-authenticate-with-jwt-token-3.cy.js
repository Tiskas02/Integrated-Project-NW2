describe(`TC-PBI17-AUTHENTICATE-WITH-JWT-TOKEN-1\n 
    Test Scenario : normal - login successfully with Student role`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('should password field be hidden.',()=>{
        cy.get('.itbkk-password').invoke('attr','type').should('match',/password/i)
    })

    it('type username and password',()=>{
        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()
    })

    it("Should show the task list page",()=>{
        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/task')
        // cy.location().should((location)=>{
        //     expect(location.pathname).to.eq('/task')
        // })
    })

    it("Should show the user's fullname -> ITBKK SOMCHAI",()=>{
        cy.get('.itbkk-username').type('itbkk.somchai')
        cy.get('.itbkk-password').type('ip23/SOM')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/task')
        // cy.location().should((location)=>{
        //     expect(location.pathname).to.eq('/task')
        // })

        cy.get('.itbkk-fullname').contains('ITBKK SOMCHAI')
    })
})