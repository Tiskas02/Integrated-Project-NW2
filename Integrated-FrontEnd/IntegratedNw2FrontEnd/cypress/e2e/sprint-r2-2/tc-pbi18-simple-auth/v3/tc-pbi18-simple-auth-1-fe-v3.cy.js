describe(`TC-PBI18-PERSIONAL-BOARD-1-FE-v3\n 
    Test Scenario : normal
                    -  /board/*
                    -  authenticated`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.sanit')
        cy.get('.itbkk-password').type('ip23/SAN')
        cy.get('.itbkk-button-signin').click()

        cy.wait(100)
        cy.url().should('contain','/board')
    }) ;

    it('Open the login page at /login and open the /board page', () => {
    })

    it('[Step 2.1] Should get statusCode 404 when opening the /board/1 page', () => {
        cy.intercept('GET','http://localhost:8080/v3/boards/1').as('request')

        cy.visit('/board/1')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(404)
        })
    })

    // it('should get statusCode 404 when opening the /board/1/task/add page', () => {
    //     cy.intercept('GET','http://localhost:8080/v3/**').as('request')

    //     cy.visit('/board/1/task/add')

    //     cy.wait('@request').then((interception) =>{
    //         const response = interception.response 
    //         expect(response.statusCode).to.equal(404)
    //     })
    // })   

    it('[Step 2.3] Should get statusCode 404 when opening the /board/1/task/1/edit page', () => {
        cy.intercept('GET','http://localhost:8080/v3/boards/1/tasks/1').as('request')

        cy.visit('/board/1/task/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(404)
        })
    })   

    it('[Step 2.4] Should get statusCode 404 when opening the /board/1/status page', () => {
        cy.intercept('GET','http://localhost:8080/v3/boards/1/statuses').as('request')

        cy.visit('/board/1/status')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(404)
        })
    })   

    // it('should get statusCode 404 when opening the /board/1/status/add page', () => {
    //     cy.intercept('GET','http://localhost:8080/v3/**').as('request')

    //     cy.visit('/board/1/status/add')

    //     cy.wait('@request').then((interception) =>{
    //         const response = interception.response 
    //         expect(response.statusCode).to.equal(404)
    //     })
    // })   

    it('[Step 2.6] Should get statusCode 404 when opening the /board/1/status/1/edit page', () => {
        cy.intercept('GET','http://localhost:8080/v3/boards/1/statuses/1').as('request')

        cy.visit('/board/1/status/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(404)
        })
    })   
    
})