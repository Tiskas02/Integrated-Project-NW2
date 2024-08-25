describe(`TC-PBI15-MATCH-PASSWORD-3\n 
    Test Scenario : failed - username is incorrect`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('should password field be hidden.',()=>{
        cy.get('.itbkk-password').invoke('attr','type').should('match',/password/i)
    })

    it('type username -> itbkk.sia and password -> ip23/SIA',()=>{
        cy.get('.itbkk-username').type('itbkk.sia')
        cy.get('.itbkk-password').type('ip23/SIA')
    })

    it('Should show a message "Username or Password is incorrect."',()=>{
        cy.get('.itbkk-username').type('itbkk.sia')
        cy.get('.itbkk-password').type('ip23/SIA')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.get('.itbkk-message').contains('Username or Password is incorrect')
    })

    it('Should show the login page',()=>{
        cy.get('.itbkk-username').type('itbkk.sia')
        cy.get('.itbkk-password').type('ip23/SIA')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/login')
        // cy.location().should((location)=>{
        //     expect(location.pathname).to.eq('/login')
        // })
    })
})