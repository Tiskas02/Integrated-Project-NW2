describe(`TC-PBI16-VALIDATE-MATCH-PASSWORD-3\n 
    Test Scenario : failed - Max+1 username and empty password`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('Type Max+1 username and should show Max username in the username field',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005MaxUser006').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
    })

    it('Empty password and The "Sign In" button should be disabled.',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005MaxUser006').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
        cy.get('.itbkk-password').clear()
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('have.class','disabled')
    })

})