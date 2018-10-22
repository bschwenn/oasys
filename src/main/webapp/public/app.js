var Person = React.createClass({

    getInitialState: function() {
        return {display: true };
    },
    handleDelete() {
        var self = this;
        $.ajax({
            url: self.props.person._links.self.href,
            type: 'DELETE',
            success: function(result) {
                self.setState({display: false});
            },
            error: function(xhr, ajaxOptions, thrownError) {
                toastr.error(xhr.responseJSON.message);
            }
        });
    },
    render: function() {

        if (this.state.display==false) return null;
        else return (
            <tr>
                <td>{this.props.person.name}</td>
                <td>{this.props.person.graduationYear}</td>
                <td>{this.props.person.email}</td>
                <td>
                    <button className="btn btn-info" onClick={this.handleDelete}>Delete</button>
                </td>
            </tr>
    );
    }
});

var PersonTable = React.createClass({

    render: function() {

        var rows = [];
        this.props.persons.forEach(function(person) {
            rows.push(
            <Person person={person} key={person.name} />);
        });

        return (
            <table className="table table-striped">
            <thead>
            <tr>
            <th>Name</th>
            <th>Graduation Year</th>
            <th>Email</th>
            <th>Delete</th>
            </tr>
            </thead>
            <tbody>{rows}</tbody>
            </table>
    );
    }
});

var App = React.createClass({

    loadPersonsFromServer: function() {

        var self = this;
        $.ajax({
            url: "http://localhost:8080/persons",
        }).then(function(data) {
            self.setState({ persons: data._embedded.persons });
        });

    },

    getInitialState: function() {
        return { persons: [] };
    },

    componentDidMount: function() {
        this.loadPersonsFromServer();
    },

    render() {
        return ( <PersonTable persons={this.state.persons} /> );
    }
});

ReactDOM.render(<App />, document.getElementById('root') );