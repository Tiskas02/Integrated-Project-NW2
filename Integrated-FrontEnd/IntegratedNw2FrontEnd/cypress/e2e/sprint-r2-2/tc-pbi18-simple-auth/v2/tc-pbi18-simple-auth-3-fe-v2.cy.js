describe(`TC-PBI18-PERSIONAL-BOARD-3-FE-v2\n 
    Test Scenario : fail
                    -  /task/*, /status/*
                    -  token is invalid (expired / not well-formed / tampered with)`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.sanit')
        cy.get('.itbkk-password').type('ip23/SAN')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
    }) ;

    it('Open the login page at /login and sign in', () => {
    })

    it('[Step 3.1] Should get statusCode 401 when opening the /task page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v2/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/task')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })

    it('[Step 3.3] Should get statusCode 401 when opening the /task/1/edit page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v2/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/task/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })
   

    it('[Step 3.4] Should get statusCode 401 when opening the /status page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v2/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/status')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })

    it('[Step 3.6] Should get statusCode 401 when opening the /status/1/edit page and redirect to /login', () => {
        cy.intercept('GET','http://localhost:8080/v2/**',{
            statusCode: 401
        }).as('request')

        cy.visit('/status/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(401)
        })

        cy.url().should('contain','/login')
    })
    
})