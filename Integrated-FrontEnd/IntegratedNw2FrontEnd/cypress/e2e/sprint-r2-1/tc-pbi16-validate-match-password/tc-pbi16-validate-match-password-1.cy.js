describe(`TC-PBI16-VALIDATE-MATCH-PASSWORD-1\n 
    Test Scenario : failed - max-length username or password`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('type max-length username and password -> The username and password should contain max-length',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
        cy.get('.itbkk-password').type('MaxPassword123').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
    })

    it('The "Sign In" button should be enabled.',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
        cy.get('.itbkk-password').type('MaxPassword123').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('not.have.class','disabled')
    })

    it('Click "Sign In" button -> Should show a message "Username or Password is incorrect."',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
        cy.get('.itbkk-password').type('MaxPassword123').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
        cy.get('.itbkk-button-signin').should('exist').click()

        cy.wait(100)
        cy.get('.itbkk-message').contains('Username or Password is incorrect')
    })
})