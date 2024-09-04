describe(`TC-PBI16-VALIDATE-MATCH-PASSWORD-1\n 
    Test Scenario : failed - Empty username and Max+1 password`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('Empty username and type Max+1 password -> The password should contain max-length',()=>{
        cy.get('.itbkk-username').clear()
        cy.get('.itbkk-password').type('MaxPassword123').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
    })

    it('The "Sign In" button should be disabled.',()=>{
        cy.get('.itbkk-username').clear()
        cy.get('.itbkk-password').type('MaxPassword123').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('have.class','disabled')
    })
})