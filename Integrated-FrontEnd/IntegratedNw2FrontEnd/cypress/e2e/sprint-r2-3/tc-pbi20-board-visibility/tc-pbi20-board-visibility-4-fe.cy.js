describe(`TC-PBI20-BOARD-VISIBILITY-1-FE\n 
    Test Scenario : normal
                    - owner can toggle board visibility`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.kittiwut')
        cy.get('.itbkk-password').type('ip23/KIT')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/board')
    }) ;

    it('[Step 1] Open the login page at /login and open the /board page', () => {
    })

    it('[Step 2] Should have board visibility button and click',()=>{
        cy.get('.itbkk-board-visibility').should('exist').as('visibility')
        cy.wait(100) 

        cy.get('@visibility').click()
    })

    it('[Step 2] Should show modal "Do you want to change board visibility to public?".',()=>{
        cy.get('.itbkk-board-visibility').should('exist').as('visibility')
        cy.wait(100) 

        cy.get('@visibility').click()

        cy.get('.itbkk-modal-alert').as('modal')

        cy.get('@modal').find('.itbkk-message').contains("Do you want to change board visibility to public?")
    })

    it('[Step 3] Should click "Confirm" button and board visibility should show "public".".',()=>{
        cy.get('.itbkk-board-visibility').should('exist').as('visibility')
        cy.wait(100) 

        cy.get('@visibility').click()

        cy.get('.itbkk-modal-alert').as('modal')

        cy.get('@modal').find('.itbkk-message').contains("Do you want to change board visibility to public?")
        cy.get('@modal').find('.itbkk-button-confirm').click()

        cy.contains('public',{matchCase: false})
    })

    it(`[Step 4 and 5] Should click "Confirm" button. The mocAPI return 403 
                        and the web shows "You do not have permission...".`,()=>{
        cy.intercept('PATCH','http://localhost:8080/v3/**',{
            statusCode: 403
        }).as('mockAPI')

        cy.get('.itbkk-board-visibility').should('exist').as('visibility')
        cy.wait(100) 

        cy.get('@visibility').click()

        cy.get('.itbkk-modal-alert').as('modal')

        cy.get('@modal').find('.itbkk-message').contains("Do you want to change board visibility to private?")
        cy.get('@modal').find('.itbkk-button-confirm').click()

        cy.wait('@mockAPI').its('response.statusCode').should('eq',403)

        cy.on('window:alert',(str)=>{
            expect(str).to.include('you do not have permission')
        })

        // cy.url().should('contain','/login')
    })

    it(`[Step 6] Should click "Confirm" button. The mocAPI return 500 
                        and the web shows "There is a problem. Please try again later.".`,()=>{
        cy.intercept('PATCH','http://localhost:8080/v3/**',{
            statusCode: 500
        }).as('mockAPI')

        cy.get('.itbkk-board-visibility').should('exist').as('visibility')
        cy.wait(100) 

        cy.get('@visibility').click()

        cy.get('.itbkk-modal-alert').as('modal')

        cy.get('@modal').find('.itbkk-message').contains("Do you want to change board visibility to private?")
        cy.get('@modal').find('.itbkk-button-confirm').click()

        cy.wait('@mockAPI').its('response.statusCode').should('eq',500)

        cy.on('window:alert',(str)=>{
            expect(str).to.equal('There is a problem. Please try again later.')
        })

        // cy.url().should('contain','/login')
    })

    it('[Step 7] Should click "Confirm" button. mocAPI should return 401',()=>{
        cy.intercept('PATCH','http://localhost:8080/v3/**',{
            statusCode: 401
        }).as('mockAPI')

        cy.get('.itbkk-board-visibility').should('exist').as('visibility')
        cy.wait(100) 

        cy.get('@visibility').click()

        cy.get('.itbkk-modal-alert').as('modal')

        cy.get('@modal').find('.itbkk-message').contains("Do you want to change board visibility to private?")
        cy.get('@modal').find('.itbkk-button-confirm').click()

        cy.wait('@mockAPI').its('response.statusCode').should('eq',401)

        // cy.contains('Access denied, you do not have permission to view this page.')

        // cy.url().should('contain','/login')
    })

})