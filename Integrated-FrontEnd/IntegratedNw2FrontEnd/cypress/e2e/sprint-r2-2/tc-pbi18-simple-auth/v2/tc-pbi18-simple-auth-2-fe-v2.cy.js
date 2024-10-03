describe(`TC-PBI18-PERSIONAL-BOARD-2-FE\n 
    Test Scenario : faile
                    -  v2/task/*, v2/status/*
                    -  not authenticated`, () => {
    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;


    it('[Step 2.1] Should redirect to /login when opening the /task page', () => {
        cy.visit('/task')

        cy.url().should('contain','/login')
    })

    it('Should redirect to /login when opening the /task/1 page', () => {
        cy.visit('/task/1')

        cy.url().should('contain','/login')
    })

    it('[Step 2.2] Should redirect to /login when opening the /task/add page', () => {
        cy.visit('/task/add')

        cy.url().should('contain','/login')
    })

    it('[Step 2.3] Should redirect to /login when opening the /task/1/edit page', () => {
        cy.visit('/task/1/edit')

        cy.url().should('contain','/login')
    })

    it('[Step 2.4] Should redirect to /login when opening the /status page', () => {
        cy.visit('/status')

        cy.url().should('contain','/login')
    }) 
 
    it('[Step 2.5] Should redirect to /login when opening the /status/add page', () => {
        cy.visit('/status/add')

        cy.url().should('contain','/login')
    }) 

    it('[Step 2.6] Should redirect to /login when opening the /status/1/edit page', () => {
        cy.visit('/status/1/edit')

        cy.url().should('contain','/login')
    }) 
    
})