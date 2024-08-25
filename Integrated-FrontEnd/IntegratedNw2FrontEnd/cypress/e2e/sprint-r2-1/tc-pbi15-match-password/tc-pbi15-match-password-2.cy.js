describe(`TC-PBI15-MATCH-PASSWORD-2\n 
    Test Scenario : failed - password is incorrect`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('should password field be hidden.',()=>{
        cy.get('.itbkk-password').invoke('attr','type').should('match',/password/i)
    })

    it('type username -> itbkk.siam and password -> itbkk.siam',()=>{
        cy.get('.itbkk-username').type('itbkk.siam')
        cy.get('.itbkk-password').type('itbkk.siam')
    })

    it('Should show a message "Password is incorrect."',()=>{
        cy.get('.itbkk-username').type('itbkk.siam')
        cy.get('.itbkk-password').type('itbkk.siam')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.get('.itbkk-message').contains('Username or Password is incorrect')
    })

    it('Should show the login page',()=>{
        cy.get('.itbkk-username').type('itbkk.siam')
        cy.get('.itbkk-password').type('itbkk.siam')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/login')
        // cy.location().should((location)=>{
        //     expect(location.pathname).to.eq('/login')
        // })
    })
})