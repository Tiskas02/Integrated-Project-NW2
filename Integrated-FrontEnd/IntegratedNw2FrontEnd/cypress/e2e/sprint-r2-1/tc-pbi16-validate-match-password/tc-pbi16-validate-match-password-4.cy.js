describe(`TC-PBI16-VALIDATE-MATCH-PASSWORD-1\n 
    Test Scenario : failed - Max+1 username and password`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;

    it('Open the login page at /login', () => {
    })

    it('type Max+1 username and password -> The username and password should contain max-length',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005MaxUser006').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
        cy.get('.itbkk-password').type('MaxPassword12345').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
    })

    it('The "Sign In" button should be enabled.',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005MaxUser006').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
        cy.get('.itbkk-password').type('MaxPassword1234').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('not.have.class','disabled')
    })

    it('Click "Sign In" button -> Should show a message "Username or Password is incorrect."',()=>{
        cy.get('.itbkk-username').type('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005MaxUser006').invoke('val').then(val=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxUser001MaxUser002MaxUser003MaxUser004MaxUser005')
        })
        cy.get('.itbkk-password').type('MaxPassword1234').invoke('val').then((val)=>{
            const myVal = val ;
            expect(myVal).to.equal('MaxPassword123')
        })
        cy.get('.itbkk-button-signin').as('button')
        cy.get('@button').should('not.have.class','disabled')
        cy.get('@button').click()

        cy.wait(100)
        cy.get('.itbkk-message').contains('Username or Password is incorrect')
    })
})