describe(`TC-PBI18-PERSIONAL-BOARD-2-FE-v3\n 
    Test Scenario : faile
                    -  /board/*
                    -  not authenticated`, () => {
    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;
    }) ;


    it('[Step 2.1] Should redirect to /login when opening the /board page', () => {
        cy.visit('/board')

        cy.url().should('contain','/login')
    })

    it('Should redirect to /login when opening the /board/1 page', () => {
        cy.visit('/board/1')

        cy.url().should('contain','/login')
    })

    it('[Step 2.2] Should redirect to /login when opening the /board/1/task/add page', () => {
        cy.visit('/board/1/task/add')

        cy.url().should('contain','/login')
    })

    it('[Step 2.3] Should redirect to /login when opening the /board/1/task/1/edit page', () => {
        cy.visit('/board/1/task/1/edit')

        cy.url().should('contain','/login')
    })

    it('[Step 2.4] Should redirect to /login when opening the /board/1/status page', () => {
        cy.visit('/board/1/status')

        cy.url().should('contain','/login')
    }) 
 
    it('[Step 2.5] Should redirect to /login when opening the /board/1/status/add page', () => {
        cy.visit('/board/1/status/add')

        cy.url().should('contain','/login')
    }) 

    it('[Step 2.6] Should redirect to /login when opening the /board/1/status/1/edit page', () => {
        cy.visit('/board/1/status/1/edit')

        cy.url().should('contain','/login')
    }) 
    
})