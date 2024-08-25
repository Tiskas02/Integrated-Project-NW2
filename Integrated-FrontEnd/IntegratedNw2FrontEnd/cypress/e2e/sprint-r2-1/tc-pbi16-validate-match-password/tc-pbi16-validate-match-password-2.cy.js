describe(`TC-PBI16-VALIDATE-MATCH-PASSWORD-2\n 
    Test Scenario : failed - empty username or password`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('Both username and password are empty -> The "Sign In" button should be disabled',()=>{
        cy.get('.itbkk-username').clear()
        cy.get('.itbkk-password').clear()
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('have.class','disabled')
    })

    it('type username and leave password empty -> The "Sign In" button should be disabled',()=>{
        cy.get('.itbkk-username').type('abc123')
        cy.get('.itbkk-password').clear()
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('have.class','disabled')
    })

    it('type password and leave username empty -> The "Sign In" button should be disabled',()=>{
        cy.get('.itbkk-username').clear()
        cy.get('.itbkk-password').type('abc123')
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('have.class','disabled')
    })
})