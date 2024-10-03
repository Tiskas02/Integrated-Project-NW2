describe(`TC-PBI18-PERSIONAL-BOARD-3-FE-v2 [step 4]\n 
    Test Scenario : fail
                    -  /task/*, /status/*
                    -  token is invalid (expired / not well-formed / tampered with)
                    -  [step 4] FE should redirect to /login without calling BE`, () => {

    it('[Step 4.1] Open the /task page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v2/**').as('request')

        cy.visit('/task')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })


    it('[Step 4.2] Open the /task/add page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v2/**').as('request')

        cy.visit('/task/add')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })


    it('[Step 4.3] Open the /task/1 page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v2/**').as('request')

        cy.visit('/task/1/edit')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })

    it('[Step 4.4] Open the /status page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v2/**').as('request')

        cy.visit('/status')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })

    it('[Step 4.5] Open the /status/add page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v2/**').as('request')

        cy.visit('/status/add')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })


    it('[Step 4.6] Open the /status/1/edit page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v2/**').as('request')

        cy.visit('/status/1/edit')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })

})