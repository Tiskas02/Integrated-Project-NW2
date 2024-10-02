describe(`TC-PBI18-PERSIONAL-BOARD-3-FE-v3 [step 4]\n 
    Test Scenario : fail
                    -  /board/*
                    -  token is invalid (expired / not well-formed / tampered with)
                    -  [step 4] FE should redirect to /login without calling BE`, () => {

    it('[Step 4.1] Open the /board page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v3/**').as('request')

        cy.visit('/board')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })


    it('[Step 4.2] Open the /board/1/task/add page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v3/**').as('request')

        cy.visit('/board/1/task/add')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })


    it('[Step 4.3] Open the /board/1/task/1 page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v3/**').as('request')

        cy.visit('/board/1/task/1/edit')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })

    it('[Step 4.4] Open the /board/1/status page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v3/**').as('request')

        cy.visit('/board/1/status')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })

    it('[Step 4.5] Open the /board/1/status/add page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v3/**').as('request')

        cy.visit('/board/1/status/add')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })


    it('[Step 4.6] Open the /board/1/status/1/edit page and should redirect to /login without calling BE.', () => {
        cy.intercept('GET','http://localhost:8080/v3/**').as('request')

        cy.visit('/board/1/status/1/edit')

        cy.wait(100)
        cy.get('@request.all').then((interception) =>{
            expect(interception).to.have.length(0) ;
        })

        cy.url().should('contain','/login')
    })

})