describe(`TC-PBI18-PERSIONAL-BOARD-1-FE\n 
    Test Scenario : normal
                    -  v2/task
                    -  v2/status
                    -  authenticated`, () => {

    beforeEach(()=> {
        cy.visit('/login') ;
        cy.wait(100) ;

        cy.get('.itbkk-username').type('itbkk.sanit')
        cy.get('.itbkk-password').type('ip23/SAN')
        cy.get('.itbkk-button-signin').click()
    }) ;

    it('[Step 2.1] Open the login page at /login and open the /task page', () => {
        cy.intercept('GET','http://localhost:8080/v2/tasks').as('request')

        cy.visit('/task')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(404)
        })

    })

    it('Should get statusCode 404 when opening the /task/1 page', () => {
        cy.intercept('GET','http://localhost:8080/v2/tasks/1').as('request')

        cy.visit('/task/1')

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

    it('[Step 2.3] Should get statusCode 404 when opening the /task/1/edit page', () => {
        cy.intercept('GET','http://localhost:8080/v2/tasks/1').as('request')

        cy.visit('/task/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(404)
        })
    })   

    it('[Step 2.4] Should get statusCode 404 when opening the /status page', () => {
        cy.intercept('GET','http://localhost:8080/v2/statuses').as('request')

        cy.visit('/status')

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

    it('[Step 2.6] Should get statusCode 404 when opening the /status/1/edit page', () => {
        cy.intercept('GET','http://localhost:8080/v2/statuses/1').as('request')

        cy.visit('/status/1/edit')

        cy.wait('@request').then((interception) =>{
            const response = interception.response 
            expect(response.statusCode).to.equal(404)
        })
    })   

})