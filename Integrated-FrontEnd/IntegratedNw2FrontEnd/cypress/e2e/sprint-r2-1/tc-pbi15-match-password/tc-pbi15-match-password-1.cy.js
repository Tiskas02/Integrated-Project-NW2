describe(`TC-PBI15-MATCH-PASSWORD-1\n 
    Test Scenario : normal - username and password are correct`, () => {

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
        cy.get('.itbkk-username').type('itbkk.olarn')
        cy.get('.itbkk-password').type('ip23/OLA')
        cy.get('.itbkk-button-signin').click()
    })
})